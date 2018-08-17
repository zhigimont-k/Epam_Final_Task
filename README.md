<h2>Cat Beauty Bar</h3>

<table style="table-layout: fixed; width:100%;">
  <tr>
    <td>A web application for an imaginary cat beauty salon.

Guests may view all of the services, as well as view individual ones and read users' reviews on them.

Registered users can add and edit reviews for services, edit user information (password, username and avatar), add money to their cards and make orders.

Making an order includes choosing a date and time of the order and choosing necessary services from the list (only those with 'available' status can be added).

Right after adding an order it has 'pending' status which changes to 'cancelled' if administrator or the user himself cancel it, or to 'confirmed' if administrator changes it or user has enough money on his card and pays for the order.

If user cancels order after paying for it, money is returned to his card.

Order's status may be set to 'finished' by the administrator.

Users may also reset their passwords by entering their email. In this case a new password is generated and a letter is sent to provided email.

Those orders which time has already passed but are still in 'pending' status are automatically cancelled after a daily check.

Those users who have confirmed orders the next day are sent reminder messages to their emails.

Administrator can add new services and update them. Administrator can also delete reviews and change users' statuses to 'admin', 'user' and 'banned'.

Banned users get an error message when they try to sign in which means they can use the service only as guests.</td>
    <td style="width:50%;"><h4>Technical description:</h4>

Java 9

JavaEE: Servlet, JSP

Server / Servlet container: tomcat7-maven-plugin 2.1

Database: MySQL, JDBC

Logger: Log4J2

Tests: TestNG

Build tool: Maven</td>
  </tr>
</table>
