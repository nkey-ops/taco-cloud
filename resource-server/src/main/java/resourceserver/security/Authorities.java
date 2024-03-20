package resourceserver.security;

public class Authorities {

  public class Ingredients {
    public static final String[] ALL = {Get.ALL, Post.INGREDIENT, Delete.INGREDIENT};
    public static final String[] GET = {Get.ALL};
    public static final String[] POST = {Post.INGREDIENT};
    public static final String[] DELETE = {Delete.INGREDIENT};

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
}
