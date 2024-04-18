package resourceserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class Authorities {
  public class Users {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
  }

  public class Ingredients {
    public static final String[] ALL = {Get.ALL, Post.INGREDIENT, Delete.INGREDIENT};
    public static final String[] GET = {Get.ALL};
    public static final String[] POST = {Post.INGREDIENT};
    public static final String[] DELETE = {Delete.INGREDIENT};

    public class Group {
      public static final String ALL = "group:allIngredients";

    }

    public class Get {
      public static final String ALL = "get:allIngredients";
    }

    public class Post {
      public static final String INGREDIENT = "post:ingredient";
    }

    public class Delete {
      public static final String INGREDIENT = "delete:ingredient";
    }
  }

  public class Groups {}

  @Bean
  RoleHierarchy roleHierarchy() {
    var roleHierarchy = new RoleHierarchyImpl();
    // roleHierarchy.setHierarchy(String.format("%s > ", Authorities.Users.USER));
    return roleHierarchy;
  }
}
