# Project Rules & Guardrails

## R1: Separation of Concerns
* **Business Logic** (validations, calculations, algorithms) must reside in independent files or classes and must be executable without browser dependency.
* The **UI Layer** is the only one authorized to interact with the DOM and manage user events.

## R2: Technical Code Standards
* **Naming:** Variables in `camelCase`, classes in `PascalCase`, and global constants in `SCREAMING_SNAKE_CASE`.
* **Declaration:** Use of `var` is prohibited. Strict use of `const` for immutable values and `let` for block-scoped variables.
* **Functions:** Preference for Arrow Functions for callbacks and class methods for structured logic.

## R3: UI, Styles, and Rendering (Tooling-Driven)
* **Tailwind CSS Ecosystem:** Tailwind must be managed via Node.js as a development dependency. Custom styles, themes, and plugins must be extended through the `tailwind.config.js` file.
* **Build Automation:** Raw Tailwind directives (`@tailwind base;`, etc.) must be processed via an npm build script to output optimized utility classes for the browser.
* **Layout:** Use of `<table>` for boards or main game layouts is prohibited. Use SVG, Canvas, or Grid/Flexbox systems.
* **Responsive:** Mandatory Mobile-First approach using Tailwind’s responsive modifiers (`md:`, `lg:`) to ensure the game functions perfectly across all device sizes.

## R4: Data, Network, and Environment Management
* **Node.js Environment:** All development orchestration—including local servers, asset bundling, and CSS compilation—must be automated using customized scripts inside the `package.json` file.
* **Fetch API:** Every network request must validate the response status (`response.ok`) and be wrapped in appropriate asynchronous error-handling blocks (`try/catch`).
* **Persistence:** Initial configuration, assets, or static game states must be fetched from local JSON files, while user states and rankings must be persisted in `localStorage
