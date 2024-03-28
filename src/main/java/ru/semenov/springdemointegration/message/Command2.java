package ru.semenov.springdemointegration.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command2<Payload> {

  private String uuid;
  private String action;
  private Payload payload;

}
