import React from "react";
import IngredientsList from "./IngredientsList";
import Instructions from "./Instructions";

export default function Recipe({
  name = "untitled",
  ingredients = [],
  steps = [],
}) {
  return (
    <section id={name.toLowerCase().replace(/ /g, "-")}>
      <h2>{name}</h2>
      <IngredientsList list={ingredients} />
      <Instructions title="烹饪说明" steps={steps} />
    </section>
  );
}
