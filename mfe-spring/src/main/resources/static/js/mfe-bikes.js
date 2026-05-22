console.log("Loading script of mfe1!")


mfe('mfe1', ({root, triggerMfeEvent}) => {
    const btns = root?.querySelectorAll('button')
    btns?.forEach(button => {
        button.addEventListener('click', () => {
            triggerMfeEvent(`add to cart bike with ${button.getAttribute("mfe-id")}`, {type: 'add', id: button.getAttribute("mfe-id")})
        });
    })
})