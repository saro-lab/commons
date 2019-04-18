package me.saro.commons.bytes.fd;

import java.lang.reflect.Method;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       3.1.0
 */
class FixedSetMethodCreator {
    
    final FixedField fixedField;
    final Method method;
    
    public FixedSetMethodCreator(FixedField fixedField) {
        this.fixedField = fixedField;
        this.method = fixedField.getSetMethod();
    }
}
