package ru.semenov.springdemointegration.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command<Request, Response> {

  private String uuid;
  private Request request;
  private String action;
  private Response response;

}
