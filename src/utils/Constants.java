package utils;

public class Constants {

	// Regular Expresions ----------------------------------------------------------------
	/*	Username should has length three at least
	 	and be composed by letters, -, @, #, $, <,
	 	>, ., :, , or ;
	 	
	 	regex: ^[A-zñ\-@#$<>.:,;]{3,}$	*/
	public static final String USERNAME_REGEX = "^[A-zñ\\-@#$<>.:,;]{3,}$";
	/*	Password should has length six at least
 		and be composed by letters, -, @, #, $,
 		<, >, ., :, , or ;	
	 	
	 	regex: ^[A-zñ\-@#$<>.:,;]{6,}$	*/
    public static final String PASSWORD_REGEX = "^[A-zñ\\-@#$<>.:,;]{6,}$";
	
	
    // Connection Status -----------------------------------------------------------------
    public static final int STATUS_CONNECTED =     1;
    public static final int STATUS_DISCONNECTED =  0;
    public static final int STATUS_ERROR =         -1;


    // Internal Messages -----------------------------------------------------------------
    // For login and logup
    public static final String RESPONSE_OK =                   "------#//u_o/k_#";
    public static final String RESPONSE_INCORRECT_PASSWORD =   "------***#pass/*word**#";
    public static final String RESPONSE_UNREGISTERED =         "------un##/r//_/_";
    public static final String RESPONSE_SERVER_FULL =          "------/n##em/p#t_y_";
    public static final String RESPONSE_DB_UNABLE_CONNECTION = "------db/#una/b_#le_";
    public static final String RESPONSE_ERROR =                "------e#rr//_";
    // For comunication
    public static final String SEND_BYE =                      "---//_/_#b#y#e#_!/_";
    public static final String SERVER_DISCONNECTION =          "------//_/_#s#b#y#e#s#_!/_";
    // MySQL
    public static final String NO_RESULTS_FOUND =              "------/NO#_d/#at-a_";
    public static final String RESULTS_FOUND =				   "------/FO_UND#_d/#at-a_";
    public static final String ALREADY_EXISTS = 	           "------/alr__e#xists##";

}
