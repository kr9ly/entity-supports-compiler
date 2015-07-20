package net.kr9ly.entitysupports.model;

import net.kr9ly.entitysupports.BuilderSupport;
import net.kr9ly.entitysupports.EqualsSupport;
import net.kr9ly.entitysupports.HashCodeSupport;
import net.kr9ly.entitysupports.ToStringSupport;

import java.util.Arrays;

/**
 * Copyright 2015 kr9ly
 * <br />
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br />
 * http://www.apache.org/licenses/LICENSE-2.0
 * <br />
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@BuilderSupport
@EqualsSupport
@HashCodeSupport
@ToStringSupport
public class SampleModel {

    SampleModel() {}

    public String stringField;

    public int intField;

    public long longField;

    public boolean boolField;

    public ChildModel childField;

    public int[] intArrayField;

    public ChildEnum enumField;

    @Override
    public boolean equals(Object o) {
        return SampleModelEquals.equals(this, o);
    }

    @Override
    public int hashCode() {
        return SampleModelHashCode.hashCode(this);
    }

    @Override
    public String toString() {
        return SampleModelToString.toString(this);
    }

    @BuilderSupport
    @EqualsSupport
    @HashCodeSupport
    @ToStringSupport
    public static class ChildModel {

        public String test;

        @Override
        public boolean equals(Object o) {
            return ChildModelEquals.equals(this, o);
        }

        @Override
        public int hashCode() {
            return ChildModelHashCode.hashCode(this);
        }

        @Override
        public String toString() {
            return ChildModelToString.toString(this);
        }
    }

    public enum ChildEnum {
        ONE,
        TWO
    }
}
