package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

public class Setting extends Entity {
        public String id;

        public String name;

        public String start_end;

        public String start_end_1;

        public String step;

        public String skip;

    public Setting(String id, String name, String start_end, String start_end_1, String step, String skip) {
        this.id = id;
        this.name = name;
        this.start_end = start_end;
        this.start_end_1 = start_end_1;
        this.step = step;
        this.skip = skip;
    }
}
