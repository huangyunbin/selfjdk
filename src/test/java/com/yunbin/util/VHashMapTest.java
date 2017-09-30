package com.yunbin.util;

import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by cloud.huang on 17/9/30.
 */
public class VHashMapTest {


    @Test
    public void setAndGetTest() {
        VHashMap map = new VHashMap();
        map.put("test", "test");
        assertThat(map).hasSize(1);
        assertThat(map.get("test")).isEqualTo("test");
    }


    @Test
    public void setAndGetTest2() {
        assertThat("FB".hashCode()).isEqualTo("Ea".hashCode());
        VHashMap map = new VHashMap();
        map.put("FB", "test1");
        map.put("Ea", "test2");
        assertThat(map).hasSize(2);
        assertThat(map.get("FB")).isEqualTo("test1");
        assertThat(map.get("Ea")).isEqualTo("test2");
    }


}
