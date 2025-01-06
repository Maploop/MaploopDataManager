package me.leon.intel;

import java.lang.annotation.*; 

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface MDB_HANDLE 
{
    String target_database() default "None";
    String target_collection() default "None";
}