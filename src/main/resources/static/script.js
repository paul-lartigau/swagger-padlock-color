/**
 * @typedef {Object} Operation
 * @property {string} [x-padlock-color] - Optional color for the padlock
 */

/**
 * @typedef {Object} PathItem
 * @property {Record<string, Operation>} [methods]
 */

/**
 * @typedef {Object} Spec
 * @property {Record<string, PathItem>} paths
 */
waitForSwaggerUI(() => processColorPadlocks());

function waitForSwaggerUI(callback) {
    if (window.ui?.specSelectors) {
        const spec = window.ui.specSelectors.specJson();
        if (spec && spec.size > 0) callback();
        else setTimeout(() => waitForSwaggerUI(callback), 50);
    } else setTimeout(() => waitForSwaggerUI(callback), 50);
}

function processColorPadlocks() {
    colorPadlocksFromSpec();

    let debounceTimer;
    const observer = new MutationObserver(() => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => colorPadlocksFromSpec(), 100);
    });

    const swaggerUI = document.querySelector("#swagger-ui");
    if (swaggerUI) {
        observer.observe(swaggerUI, {
            childList: true,
            subtree: true,
        });
    }
}

function colorPadlocksFromSpec() {
    const specObj = window.ui?.specSelectors?.specJson();
    if (!specObj) return;

    /** @type {Spec} */
    const specJS = specObj.toJS();
    if (!specJS.paths) return;

    Object.entries(specJS.paths).forEach(([path, methods]) => {
        Object.entries(methods).forEach(([, operation]) => {
            if (!operation) return;

            const padlockColor = operation["x-padlock-color"];
            if (!padlockColor) return;

            document.querySelectorAll(`span[data-path='${path}']`).forEach((span) => {
                const opblock = span.closest(".opblock");
                if (!opblock) return;

                const padlockSVG = opblock.querySelector("svg.unlocked, svg.locked");
                if (!padlockSVG) return;

                if (padlockSVG.getAttribute("fill") !== padlockColor) {
                    padlockSVG.setAttribute("fill", padlockColor);
                }
            });
        });
    });
}
