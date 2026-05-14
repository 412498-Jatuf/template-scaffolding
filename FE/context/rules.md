### **Project Rules & Guardrails**

#### **R1: Separation of Concerns **
* **Business Logic** (validations, calculations, algorithms) must reside in independent files or classes and must be executable without browser dependency.
* The **UI Layer** is the only one authorized to interact with the DOM and manage user events.

#### **R2: Technical Code Standards**
* **Naming:** Variables in `camelCase`, classes in `PascalCase`, and global constants in `SCREAMING_SNAKE_CASE`.
* **Declaration:** Use of `var` is prohibited. Strict use of `const` for immutable values and `let` for block-scoped variables.
* **Functions:** Preference for Arrow Functions for callbacks and class methods for structured logic.

#### **R3: UI, Styles, and Rendering**
* **Layout:** Use of `<table>` for the board or game logic is prohibited. Use SVG, Canvas, or Grid/Flexbox systems.
* **Tailwind CSS:** Exclusive use of utility classes directly in the HTML. Avoid extensive custom CSS files.
* **Responsive:** Mandatory Mobile-First approach to ensure the game works on any device.

#### **R4: Data and Network Management**
* **Fetch API:** Every request must validate the response status and be wrapped in error-handling blocks.
* **Persistence:** Initial configuration and rankings must be managed via local JSON files and persisted in `localStorage`.