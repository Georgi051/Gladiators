function searchByText() {
    let section = document.getElementById('products');
    section.querySelectorAll('*').forEach(n => n.remove());
    fetch("/api/products/all")
        .then((response) => response.json())
        .then((products) => productsAppend(products));
}

function sortProducts() {
    let select = document.getElementById("sort-select");
    let sortType = select.options[select.selectedIndex].value;
    fetch("/api/products/all")
        .then((response) => response.json())
        .then((products) => {
                switch (sortType) {
                    case "alphabeticallyAscending":
                        products.sort();
                        productsAppend(products);
                        break;
                    case "alphabeticallyDescending":
                        products.sort().reverse();
                        productsAppend(products);
                        break;
                    case "priceAscending":
                        products.sort((a, b) => a.price - b.price);
                        productsAppend(products);
                        break;
                    case "priceDescending":
                        products.sort((a, b) => b.price - a.price);
                        productsAppend(products);
                        break;
                    case "byMostPopular":
                        products.sort((a, b) => b.buyingCounter - a.buyingCounter);
                        productsAppend(products);
                        break;
                }

        }
        )
}

function filterByHigherAndLowerPrice() {
    fetch("/api/products/all")
        .then((response) => response.json())
        .then((products) => {;
            productsAppend(filterByPrice(products));
        }
        )

}

function filterByManufacturers() {
    let manufacturer = document.querySelectorAll('.custom-checkbox:checked');
    let filteredProducts = [];
    fetch("/api/products/all")
        .then((response) => response.json())
        .then((products) => {

            if(manufacturer.length === 0){
                filteredProducts = products;
            }else {
                manufacturer.forEach((manufacturer) => {

                    products.forEach((product) => {
                        if (product.manufacturerName === manufacturer.value) {
                            filteredProducts.push(product);
                        }
                    })

                })
            }
            productsAppend(filteredProducts);
        })
}

function productsAppend(products) {
    let section = document.getElementById('products');
    section.querySelectorAll('*').forEach(n => n.remove());
    let search = document.getElementById("search");
    let offers = [];
    fetch("/api/offers/all")
        .then((response) => response.json())
        .then(topOffers => {
             offers = topOffers;
             products = filterByPrice(products);
             products.forEach((product) => {
             if (product.name.toLowerCase().includes(search.value.toLowerCase())) {
                let article = document.createElement('article');
                article.classList.add('product-article');
                let img = document.createElement('img');
                img.src = product.imageUrl;
                img.alt = '';
                let name = document.createElement('span');
                name.textContent = product.name;
                article.appendChild(img);
                article.appendChild(name);
                offers.forEach((offer) => {
                    if(offer.product.id === product.id){
                        let oldPrice = document.createElement('span');
                        oldPrice.classList.add('product-old-price');
                        oldPrice.textContent = product.price + ' $';
                        let offerPrice = document.createElement('span');
                        offerPrice.textContent = offer.price + ' $';
                        article.appendChild(oldPrice);
                        article.appendChild(offerPrice);
                    }
                });
                let price = document.createElement('span');
                price.textContent = product.price.toFixed(2) + ' $';
                article.appendChild(price);
                let a = document.createElement('a');
                a.href = "/products/details/" + product.id;
                a.textContent = 'Details';
                article.appendChild(a);
                section.appendChild(article);
            }
        }
    )});
}

function filterByPrice(products) {
    let higherThan = document.getElementById('minPrice').value;
    let lowerThan = document.getElementById('maxPrice').value;
    if(higherThan !== '' && lowerThan !== ''){
        products = products.filter((product) => product.price >= higherThan && product.price <= lowerThan);
    }else if(higherThan !== ''){
        products = products.filter((product) => product.price >= higherThan);
    }else if(lowerThan !== ''){
        products = products.filter((product) => product.price <= lowerThan);
    }
    return products;
}

