package marcio.marketplace.registration.domain;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class Customer {

    private CustomerId id;
    private String name;
    private String email;

    // construtor principal -> mais completo, onde acontece a logica
    public Customer(CustomerId id, String name, String email) {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(email, "email must not be null");

        this.id = id;
        this.name = name;
        this.email = email;
//
    }

    // construtor simplificado -> Isso é chamado de: encadeamento de construtores...
    public Customer(String name, String email){
        this(new CustomerId(), name, email); //cria um novo ID e depois chama o construtor principal
    }

}
