package net.kr9ly.entitysupports;

import net.kr9ly.entitysupports.model.ChildModelBuilder;
import net.kr9ly.entitysupports.model.SampleModel;
import net.kr9ly.entitysupports.model.SampleModelBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

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
@RunWith(JUnit4.class)
public class EntitySupportsProcessorTest {

    @Test
    public void testBuilderSupport() {
        SampleModel model = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        assertEquals(1, model.intField);
        assertEquals("test", model.stringField);
        assertEquals(5, model.longField);
        assertEquals(3, model.intArrayField[1]);
        assertEquals("child", model.childField.test);
        assertEquals(SampleModel.ChildEnum.ONE, model.enumField);
    }

    @Test
    public void testEqualsSupport() {
        SampleModel model1 = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .boolField(true)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        SampleModel model2 = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .boolField(true)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        assertFalse(model1.equals(null));
        assertTrue(model1.equals(model2));

        model2.intField = 2;
        assertFalse(model1.equals(model2));
        reset(model2);
        model2.stringField = "test2";
        assertFalse(model1.equals(model2));
        reset(model2);
        model2.longField = 4;
        assertFalse(model1.equals(model2));
        reset(model2);
        model2.childField.test = "child2";
        assertFalse(model1.equals(model2));
        reset(model2);
        model2.intArrayField[0] = 4;
        assertFalse(model1.equals(model2));
        reset(model2);
        model2.enumField = SampleModel.ChildEnum.TWO;
        assertFalse(model1.equals(model2));

        reset(model2);
        assertTrue(model1.equals(model2));
    }

    @Test
    public void testHashCodeSupport() {
        SampleModel model1 = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .boolField(true)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        SampleModel model2 = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .boolField(true)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        assertEquals(model1.hashCode(), model2.hashCode());

        model2.intField = 2;
        assertNotEquals(model1.hashCode(), model2.hashCode());
        reset(model2);
        model2.stringField = "test2";
        assertNotEquals(model1.hashCode(), model2.hashCode());
        reset(model2);
        model2.longField = 4;
        assertNotEquals(model1.hashCode(), model2.hashCode());
        reset(model2);
        model2.childField.test = "child2";
        assertNotEquals(model1.hashCode(), model2.hashCode());
        reset(model2);
        model2.intArrayField[0] = 4;
        assertNotEquals(model1.hashCode(), model2.hashCode());
        reset(model2);
        model2.enumField = SampleModel.ChildEnum.TWO;
        assertNotEquals(model1.hashCode(), model2.hashCode());
        reset(model2);
        model2.boolField = false;
        assertNotEquals(model1.hashCode(), model2.hashCode());

        reset(model2);
        assertEquals(model1.hashCode(), model2.hashCode());
    }

    @Test
    public void testToStringSupport() {
        SampleModel model1 = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .boolField(true)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        SampleModel model2 = SampleModelBuilder.newBuilder()
                .intField(1)
                .stringField("test")
                .longField(5)
                .boolField(true)
                .childField(ChildModelBuilder.newBuilder()
                        .test("child").build())
                .intArrayField(new int[]{2, 3})
                .enumField(SampleModel.ChildEnum.ONE)
                .build();

        assertEquals(model1.toString(), model2.toString());

        model2.intField = 2;
        assertNotEquals(model1.toString(), model2.toString());
        reset(model2);
        model2.stringField = "test2";
        assertNotEquals(model1.toString(), model2.toString());
        reset(model2);
        model2.longField = 4;
        assertNotEquals(model1.toString(), model2.toString());
        reset(model2);
        model2.childField.test = "child2";
        assertNotEquals(model1.toString(), model2.toString());
        reset(model2);
        model2.intArrayField[0] = 4;
        assertNotEquals(model1.toString(), model2.toString());
        reset(model2);
        model2.enumField = SampleModel.ChildEnum.TWO;
        assertNotEquals(model1.toString(), model2.toString());
        reset(model2);
        model2.boolField = false;
        assertNotEquals(model1.toString(), model2.toString());

        reset(model2);
        assertEquals(model1.toString(), model2.toString());
    }

    private void reset(SampleModel model) {
        model.intField = 1;
        model.stringField = "test";
        model.longField = 5;
        model.boolField = true;
        model.childField = ChildModelBuilder.newBuilder().test("child").build();
        model.intArrayField = new int[]{2,3};
        model.enumField = SampleModel.ChildEnum.ONE;
    }
}
