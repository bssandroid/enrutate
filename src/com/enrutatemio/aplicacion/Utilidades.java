package com.enrutatemio.aplicacion;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase con utilidades varias para aplicacion
 *
 */
public class Utilidades {
	
	
        /**
         * Metodo que retorna la fecha en formato yyyymmdd
         * @return
         */
        public static String getCurrentTimeStampyyyyMMdd() {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);
            return strDate;
        }
    

       
}

