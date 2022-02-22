package org.greeting;

import lombok.RequiredArgsConstructor;
import org.greeting.model.Language;
import org.greeting.service.GreetingService;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Hello {

    private final GreetingService greetingService;

    public static void main(String[] args) {
        WeldContainer container = new Weld()
                .enableDiscovery()
                .addPackage(true, Hello.class)
                .initialize();

        container.select(Hello.class).get().run();
    }

    public void run() {
        greetingService.sayHello(Language.EN);
    }
}
