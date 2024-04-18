package authserver.controller.converter;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import authserver.controller.model.ClientRegistration;

public class RegistrationClientHttpMessageConverter
    extends AbstractHttpMessageConverter<ClientRegistration> {

  @Override
  protected boolean supports(Class<?> clazz) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'supports'");
  }

  @Override
  protected ClientRegistration readInternal(Class<? extends ClientRegistration> clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'readInternal'");
  }

  @Override
  protected void writeInternal(ClientRegistration t, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'writeInternal'");
  }



}
