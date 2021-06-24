package com.lululemon.cart.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static com.lululemon.cart.constant.CartActionConstant.*;
import static java.util.Arrays.asList;

public class CartActionValidator implements ConstraintValidator<ValidAction, String> {

    List<String> validActions = asList(CREATE_CART, UPDATE_LINE_ITEM_QUANTITY, REMOVE_LINE_ITEM, ADD_LINE_ITEM);

    @Override
    public boolean isValid(String action, ConstraintValidatorContext constraintValidatorContext) {
        return validActions.contains(action);
    }
}
