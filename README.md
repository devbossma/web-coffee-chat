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

Then open `http://localhost:8080/coffeeshop`. One thing to know: Jetty's plugin always deploys at
the root path (`/`), so the URL here is shorter than the one below. That is only true of this
dev-testing shortcut, not of a real deployment.

### Running it the way the assignment describes

Install **Tomcat 10.1 or newer** (this app uses the `jakarta.servlet.*` namespace, which needs
Tomcat 10.1+ / Jakarta EE 10, not an older `javax.servlet` Tomcat 9), drop
`target/coffee-shop-app.war` into `<tomcat>/webapps/`, start Tomcat, and open:

```
http://localhost:8080/coffee-shop-app/coffeeshop
```

Tomcat auto-deploys any WAR dropped into `webapps/` using its filename as the context path, so
that URL works as soon as the file lands there.

### Trying the barista side

Go to `/barista/login` and sign in with `barista` / `coffee123`, the one hardcoded credential
pair this app ships with. From there, `/barista/console` shows every open chat and lets you reply
to any of them, and `/barista/orders` is where an order actually moves from `PLACED` to `PAID` to
`FULFILLED`.

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
