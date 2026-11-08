This project manages its dependencies (the servlet API, JSTL) through Maven's pom.xml instead of
jars dropped in here by hand. This folder exists only to match the assignment's own directory
layout; `mvn package` resolves everything it needs and bundles the JSTL implementation into
WEB-INF/lib inside the built WAR automatically.
