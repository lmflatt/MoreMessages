import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static Map<String, User> users = new HashMap();
    static User user;
    static Message message;
    public static String warning = "";
    public static ArrayList<Message> messageList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.get("/",
                ((request, response) -> {
                    Map m = new HashMap();
                    if (user == null) {
                        m.put("warning", warning);
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("name", user.getName());
                        if (!user.getMessages().isEmpty()) {
                            messageList = user.getMessages();
                        }
                        m.put("messages", messageList);
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

                    if (users.containsKey(name)) {

                        user = users.get(name);
                        if (password.equalsIgnoreCase(user.getPassword())) {
                            response.redirect("/");
                        } else {
                            user = null;
                            warning = "Incorrect Login Information.";
                            response.redirect("/");
                        }
                    } else {
                        warning = "User does not exist.";
                        response.redirect("/");
                    }
                    return "";
                })
        );


        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("newName");
                    String password = request.queryParams("newPassword");

                    if (users.containsKey(name)) {
                        response.redirect("/");
                        warning = "User Name is already taken.";
                    } else {
                        user = new User(name, password);
                        users.put(name, user);
                        response.redirect("/");
                    }
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String userMessage = request.queryParams("message");
                    if (!user.getMessages().isEmpty()) {
                        messageList = user.getMessages();
                    }
                    message = new Message(userMessage);
                    messageList.add(message);
                    user.setMessages(messageList);
                    users.put(user.getName(), user);
                    response.redirect("/");

                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    user = null;
                    messageList = new ArrayList<>();
                    response.redirect("/");

                    return "";
                })
        );


    }
}
