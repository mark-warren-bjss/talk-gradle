package org.greeting;

import org.greeting.service.GreetingService;

public class Hello {
    public static void main(String[] args) {
        GreetingService greetingService = new GreetingService();

        greetingService.sayHello();
    }
}
