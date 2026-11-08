# Welcome to Coffee Shop
***

## Task

This project is "I want my Coffee on the Web!": a coffee shop ordering app built with plain Java
Servlets, JSP, and the MVC pattern, packaged as a WAR and deployed on a servlet container. No
Spring, no database, no multithreading, none of that is asked for here. The assignment's own
example code keeps everything in memory (a fixed coffee menu and a running list of orders), and
this project follows that lead on purpose rather than reaching for a bigger stack than the task
actually needs.

The assignment's overview also calls for a way to chat with a barista, "simulated via form
submissions", without showing any code for it. That piece was designed and built from scratch here:
a chat tied to the customer's own order, open for exactly as long as that order is being prepared,
plus a small shared console where a logged-in barista can see every open chat and the kitchen's
order queue.

## Description

### The pieces

| Layer | What's in it |
|---|---|
| `src/model` | Plain data: `Coffee`, `Order`/`OrderStatus`, `ChatSession`/`ChatSessionStatus`, `ChatMessage`/`MessageSender` |
| `src/service` | `CoffeeShopService` (the menu and the order list), `ChatService` (sessions and transcripts), `CustomerIdGenerator` (receipt ids), `BaristaAuthService` (one in-memory credential pair) |
| `src/controller` | One servlet per concern: ordering, the public order board, the customer chat, barista login, the barista chat console, and the barista's order actions |
| `src/filter` | `BaristaAuthFilter`, guarding everything under `/barista` except the login page itself |
| `src/listener` | `AppInitListener`, builds each service exactly once at startup and hands the same instance to every servlet via the `ServletContext` |
| `web/WEB-INF` | `web.xml` (every servlet, filter, and listener is wired here, not through annotations, so there is one place to read the whole app's routing) and the JSP views |

### How an order and a chat actually connect

1. The first time anyone opens the shop, they're handed a receipt-style id in their session, for
   example `20261108-003`: today's date plus a counter that resets each day. There are no
   accounts, no names, no passwords for customers, this id is the only thing that identifies them
   anywhere in the app.
2. Placing an order does two things in the same request: it adds the order to
   `CoffeeShopService`'s list, and it opens a brand new chat session in `ChatService`, linked to
   that order's id. The customer is redirected straight into that chat.
3. While the order sits as `PLACED` then `PAID`, the customer can type to "the barista" (there is
   exactly one shared console, not a roster of baristas to be matched against, this is a
   single-counter kiosk) and the barista can reply from `/barista/console`.
4. The moment a barista marks the order `FULFILLED` from `/barista/orders`, that order's chat
   session is closed right there, a plain method call, nothing resembling a real event bus. The
   customer's own chat page polls every few seconds (there is no WebSocket here, plain JSP has no
   way to push to the browser, and the assignment itself only asks for chat "simulated via form
   submissions"), and the next time it notices the session is closed, it redirects back to the
   menu on its own.

### Why a few things are built the way they are

- **`CopyOnWriteArrayList` and `AtomicInteger` instead of a plain `ArrayList` and `int`.** The
  servlet container runs every request on its own thread by default, even though nothing in this
  app is hand-threaded, so two customers placing an order at the exact same moment is a real
  scenario, not a hypothetical one.
- **One `synchronized` method on `CustomerIdGenerator`.** Handing out the next id is a
  check-the-date-then-increment-the-counter operation on two fields together, which is exactly the
  case a plain `AtomicInteger` can't express by itself (it only makes one counter's own increment
  atomic, not "increment this counter, unless the date just rolled over, in which case reset it
  first").
- **Every servlet and filter is declared in `web.xml`, not with `@WebServlet`/`@WebFilter`.** The
  assignment's own example asks for a hand-written `web.xml`, and keeping every mapping there
  (instead of splitting them between annotations and the descriptor) means there is exactly one
  place to check what maps to what.
- **The chat entry point is a floating button, not a persistent pop-up panel.** It only appears
  once a customer actually has an order being prepared, and clicking it still just navigates to
  the full `/chat` page. A real always-on floating chat widget would need AJAX polling and shared
  markup injected into every page, real complexity this assignment doesn't ask for; this gets the
  same "support bubble" feel without it.

## Installation

You will need **JDK 21 or newer** and **Maven**. There is no database and nothing else to install,
everything the app needs lives in memory for as long as the server is running.

```bash
git clone https://github.com/devbossma/web-coffee-chat.git
cd web-coffee-chat
mvn clean package
```

This produces `target/coffee-shop-app.war`.

## Usage

### Trying it quickly, without installing a servlet container

The project includes Jetty's Maven plugin purely as a local shortcut, so you can see it running
without setting up Tomcat first:

```bash
mvn org.eclipse.jetty.ee10:jetty-ee10-maven-plugin:12.0.15:run-war
```

Then open `http://localhost:8081/coffeeshop`. Two things to know, both only true of this
dev-testing shortcut, not of a real deployment:
- Jetty's plugin always deploys at the root path (`/`), so the URL here is shorter than the one
  below.
- It is configured in `pom.xml` to listen on port **8081**, not the usual 8080, specifically so it
  does not collide with a real Tomcat instance also running on 8080 for the walkthrough below.
  You can run both at the same time without either one fighting over the port.

### Running it the way the assignment describes, step by step

This app uses the `jakarta.servlet.*` namespace, which needs **Tomcat 10.1 or newer** (Jakarta EE
10). An older Tomcat 9, which still speaks `javax.servlet`, will not load it.

1. **Get Tomcat.** Download the **Core / 64-bit Windows zip** (or the plain Core `.zip` on
   Linux/macOS) from `https://tomcat.apache.org/download-10.cgi`, a 10.1.x release. Skip the
   Windows installer `.exe`, it installs Tomcat as a background service, which is more than this
   needs.
2. **Extract it** somewhere simple, for example `C:\tomcat10` (Windows) or `~/tomcat10`
   (Linux/macOS). You should see `bin`, `webapps`, `conf`, and `logs` folders inside.
3. **Set `JAVA_HOME` and `CATALINA_HOME` as persistent environment variables, not just for your
   current session.** Tomcat's startup/shutdown scripts can guess both on their own, but only by
   looking at your terminal's *current directory*, not at where the script itself lives or where
   Java is actually installed. So running them from anywhere other than Tomcat's own `bin` folder
   fails with one of:
   ```
   Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
   The CATALINA_HOME environment variable is not defined correctly
   ```
   Set both once, permanently, and this stops mattering:
   ```
   setx JAVA_HOME "C:\Program Files\Java\jdk-21"     (Windows, then open a NEW terminal)
   setx CATALINA_HOME "C:\tomcat10"                  (Windows, then open a NEW terminal)

   export JAVA_HOME=/path/to/your/jdk                (Linux/macOS, add to ~/.bashrc or ~/.zshrc)
   export CATALINA_HOME=~/tomcat10                   (Linux/macOS, add to ~/.bashrc or ~/.zshrc)
   ```
   `setx` only updates the variable for terminals opened after you run it, your current window
   still has the old (empty) value, so close it and open a fresh one before continuing.
4. **Build the WAR:**
   ```bash
   git clone https://github.com/devbossma/web-coffee-chat.git
   cd web-coffee-chat
   mvn clean package
   ```
   This produces `target/coffee-shop-app.war`.
5. **Drop the WAR into Tomcat's `webapps/` folder:**
   ```
   copy target\coffee-shop-app.war C:\tomcat10\webapps\      (Windows)
   cp target/coffee-shop-app.war ~/tomcat10/webapps/         (Linux/macOS)
   ```
   Tomcat auto-deploys anything dropped into `webapps/`, using the filename as the context path,
   within a few seconds, no restart needed if Tomcat is already running.
6. **Start Tomcat** (if it is not already running):
   ```
   C:\tomcat10\bin\startup.bat      (Windows)
   ~/tomcat10/bin/startup.sh        (Linux/macOS)
   ```
7. **Open the app:**
   ```
   http://localhost:8080/coffee-shop-app/coffeeshop
   ```
8. **Stop Tomcat** when done:
   ```
   C:\tomcat10\bin\shutdown.bat     (Windows)
   ~/tomcat10/bin/shutdown.sh       (Linux/macOS)
   ```

If port 8080 is already taken (for instance by another Tomcat instance, since the Jetty shortcut
above was deliberately moved to 8081 to avoid exactly this), startup will fail silently or log a
bind error in `logs/catalina.<date>.log`, stop whatever is holding the port first.

### Trying the barista side

Go to `/barista` (it redirects to `/barista/login` if you are not signed in yet, or straight to
the console if you already are) and sign in with `barista` / `coffee123`, the one hardcoded
credential pair this app ships with. From there, `/barista/console` shows every open chat and
lets you reply to any of them, `/barista/orders` is where an order actually moves from `PLACED`
to `PAID` to `FULFILLED`, and `/barista/logout` (also linked from both pages) signs you back out.

### Known things worth knowing before you go looking for them as bugs

- Chat and barista pages refresh every 5 seconds on their own (a plain `<meta http-equiv="refresh">`,
  no JavaScript). An update on one side can take up to that long to show up on the other.
- If you rebuild (`mvn clean package`) while a previous `jetty:run-war` is still running, the clean
  step will fail because the running server is still holding its own copied jars open. Stop the
  server first.
- There is no payment processor and no refund flow, "mark paid" simply records that payment
  happened, the same deliberate simplification the original plain-Java coffee shop project made.
- A barista session is just a flag in that browser's own `HttpSession`, there is no real user
  database behind it, so it does not survive switching browsers or clearing cookies.

### The Core Team


<span><i>Made at <a href='https://qwasar.io'>Qwasar SV -- Software Engineering School</a></i></span>
<span><img alt='Qwasar SV -- Software Engineering School's Logo' src='https://storage.googleapis.com/qwasar-public/qwasar-logo_50x50.png' width='20px' /></span>
