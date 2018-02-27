package com.example.jgchan.datemypet.entidades;

import java.util.List;
import java.util.Map;

/**
 * Created by jgchan on 26/02/18.
 */

public class ApiError {


     String message;
         Map<String, List<String>> errors;

         public String getMessage() {
             return message;
         }

         public Map<String, List<String>> getErrors() {
             return errors;
         }


}
