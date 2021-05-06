package ch.so.agi.healthcheck.probe;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Repeatable(ParamDefinitions.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamDefinition {
    String name();
    String description();
   

}
