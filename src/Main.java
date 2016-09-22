import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static Map<String, User> users = new HashMap();
    public static String warning = "";
//    public static ArrayList<Message> messageList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.get("/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = users.get(name);
                    HashMap m = new HashMap();

                    if (user == null) {
                        m.put("warning", warning);
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("name", user.getName());
//                        if (!user.getMessages().isEmpty()) {
//                            messageList = user.getMessages();
//                        }
                        m.put("messages", user.getMessages());
                        m.put("image", "css/profile.jpg");
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    String password = request.queryParams("password");
                    User user = users.get(name);

                    if (user != null) {

                        if (password.equalsIgnoreCase(user.getPassword())) {
                            warning = "";
                            Session session = request.session();
                            session.attribute("loginName", name);
                        } else {
                            user = null;
                            warning = "Incorrect Login Information.";
                        }
                    } else {
                        warning = "User does not exist.";
                    }

                    response.redirect("/");
                    return "";
                })
        );


        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("newName");
                    String password = request.queryParams("newPassword");

                    if (name.length() > 0 && password.length() > 0) {
                        if (users.containsKey(name)) {
                            warning = "User Name is already taken.";
                        } else {
                            users.put(name, new User(name, password));
                            Session session = request.session();
                            session.attribute("loginName", name);
                        }
                    } else {
                        warning = "Your username or password was left blank.";
                    }
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = users.get(name);
                    if (user == null) {
                        throw new Exception("User is not logged in!");
                    }

                    String userMessage = request.queryParams("message");
//                    if (!user.getMessages().isEmpty()) {
//                        messageList = user.getMessages();
//                    }
                    Message message = new Message(userMessage);
                    user.getMessages().add(0, message);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    Message message = new Message(request.queryParams("userMessage"));
                    User user = users.get(name);

                    if (user == null) {
                        throw new Exception("How are you not logged in?");
                    } else {
//                        messageList = user.messages;
                        for (Message m : user.getMessages()) {
                            if (m.getMessage().equals(message.getMessage())) {
                                int index = user.getMessages().indexOf(m);
                                user.getMessages().remove(index);
                                response.redirect("/");
                            }
                        }
                    }


                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );


    }
}
