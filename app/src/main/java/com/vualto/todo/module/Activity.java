package com.vualto.todo.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by David on 07/09/2015.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface Activity {
}
