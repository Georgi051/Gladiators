function loadSubCategories(id) {

    let li = document.getElementById(id);
    if(li.children.length === 1){
        let ul = document.createElement("ul");
        ul.classList.add("sub-categories")
        fetch("/api/categories/all")
            .then((response) => response.json())
            .then((categories) => {
                categories.forEach((category) => {
                    if(category.id === id){

                        category.subCategories.forEach(subCategory => {
                            let a = document.createElement("a");
                            a.classList.add("nav-link");
                            a.value = subCategory.id;
                            a.text = subCategory.name;
                            a.href = "/subcategories/" + subCategory.id;
                            let li = document.createElement('li');
                            li.appendChild(a);
                            ul.appendChild(li);
                        })
                    }

                })
            })
        li.appendChild(ul);
        document.getElementById("nav-shop").replaceChild(document.getElementById(id), li);
    }else {
        li.lastChild.remove();
    }

}