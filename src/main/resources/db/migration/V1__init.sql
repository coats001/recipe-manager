CREATE TABLE recipes (
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         vegetarian BOOLEAN NOT NULL,
                         servings INTEGER NOT NULL,
                         instructions TEXT NOT NULL
);

CREATE TABLE recipe_ingredients (
                                    recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
                                    ingredient TEXT NOT NULL,
                                    PRIMARY KEY (recipe_id, ingredient)
);