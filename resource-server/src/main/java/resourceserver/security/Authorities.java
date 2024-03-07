package resourceserver.security;

import org.springframework.security.core.GrantedAuthority;

public interface Authorities {
  String[] authorities();

  public enum Ingredients implements Authorities{
    ALL {
      private final String[] authorities = getAuthorities(); 

      @Override
      public String[] authorities() {
        return authorities.clone();
      }

      private String[] getAuthorities() {
        Read[] reads = Read.values();
        Create[] creates = Create.values();
        String[] values = new String[reads.length + creates.length];

        for (int i = 0, c = 0; i < values.length; i++) {
          if(i < reads.length)
            values[i] = reads[i].getAuthority();
          else
            values[i] = creates[c++].getAuthority();
        }

        return values;
      }
    },

    READ {
      private final String[] authorities = getAuthorities(); 

      @Override
      public String[] authorities() {
        return authorities.clone();
      }

      public String[] getAuthorities() {
        String[] values = new String[Read.values().length];

        for (int i = 0; i < values.length; i++) {
          values[i] = Read.values()[i].getAuthority();
        }

        return values;
      }
    },

    CREATE {
      private final String[] authorities = getAuthorities(); 

      @Override
      public String[] authorities() {
        return authorities.clone();
      }

      public String[] getAuthorities() {
        String[] values = new String[Create.values().length];

        for (int i = 0; i < values.length; i++) {
          values[i] = Create.values()[i].getAuthority();
        }

        return values;
      }
    };


    public static enum Read implements GrantedAuthority {
      ALL_INREDIENTS("read:allIngredients");

      private final String authority;

      Read(String authority) {
        this.authority = authority;
      }

      @Override
      public String getAuthority() {
        return authority;
      }
    }

    public static enum Create implements GrantedAuthority {
      INREDIENT("create:ingredient");

      private final String authority;

      Create(String authority) {
        this.authority = authority;
      }

      @Override
      public String getAuthority() {
        return authority;
      }
    }
  }
}
