import React from "react";
import Recipe from "./Recipe";
import "./Menu.css";

export default function Menu({ recipes = [] }) {
  return (
    <article>
      <header>
        <h1>美食食谱</h1>
      </header>
      <div className="recipes">
        {recipes.map((props, i) => (
          <Recipe key={i} {...props} />
        ))}
      </div>
    </article>
  );
}
