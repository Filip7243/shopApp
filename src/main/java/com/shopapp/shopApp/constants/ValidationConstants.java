package com.shopapp.shopApp.constants;

public class ValidationConstants {

    public static final String NAME_REQUIRED = "Name is required!";
    public static final String DESCRIPTION_REQUIRED = "Description is required!";
    public static final String LAST_NAME_REQUIRED = "Last name is required!";
    public static final String EMAIL_REQUIRED = "Email is required!";
    public static final String PASSWORD_REQUIRED = "Password is required!";
    public static final String PHONE_NUMBER_REQUIRED = "Phone number is required!";
    public static final String ADDRESS_REQUIRED = "Address is required!";
    public static final String TOPIC_REQUIRED = "Topic is required!";
    public static final String VALID_MIN_VALUE = "Min value is 1!";
    public static final String VALID_MAX_VALUE = "Max value is 5!";
    public static final String VALID_NAME_MAX_LENGTH = "Name is too long. Max length is 35 characters";
    public static final String VALID_DESCRIPTION_MAX_LENGTH = "Description is too long. Max length is 255 characters";
    public static final String PASSWORD_NOT_VALID = "Password is not valid, it should contain:" +
                                                    " a lower case letter at least once," +
                                                    " an upper case letter at least once," +
                                                    " a special character at least once," +
                                                    " no whitespace," +
                                                    " between 8 and 50 characters.";


    // REGEX
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,50}$";
    public static final String PHONE_NUMBER_REGEX = "^\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\W*\\d\\W*\\d\\W*\\d\\W*\\d\\W*\\d\\W*\\d\\W*\\d\\W*\\d\\W*(\\d{1,2})$";
    public static final String EMAIL_REGEX = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                                             "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
                                             "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                                             "\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                                             "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";
}
