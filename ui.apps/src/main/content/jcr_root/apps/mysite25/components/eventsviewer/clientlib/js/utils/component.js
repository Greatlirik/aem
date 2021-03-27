com.github.component.Component = function (options) {
    this.$self = options.$self
    this.selectors = com.github.component.constants.SELECTORS
    this.dependencies = options.dependencies
    this.init()
};

com.github.component.Component.prototype.init = function () {
    var self = this;
    var $template = this.$self.children(this.selectors.TEMPLATE)
    if ($template.length === 0) {
        throw new Error("Template not found");
    }

    Handlebars.registerHelper('pagination', function (size, options) {
        var currentPage = Number(this.page);
        var pageCount = Number(this.pageCount);
        var isFirstPage = currentPage === 1;
        var isLastPage = currentPage == pageCount;

        var startPage = currentPage - Math.floor(size / 2);
        var endPage = currentPage + Math.floor(size / 2);

        if (startPage <= 0) {
            endPage -= (startPage - 1);
            startPage = 1;
        }

        if (endPage > pageCount) {
            endPage = pageCount;
            startPage = endPage - size + 1 > 0 ? endPage - size + 1 : 1;
        }

        var context = {
            prevClass: isFirstPage ? "disabled" : "page-button",
            prevPage: currentPage - 1,
            pages: [],
            nextClass: isLastPage ? "disabled" : "page-button",
            nextPage: currentPage + 1,
            pageCount: pageCount
        };

        for (var i = startPage; i <= endPage; i++) {
            context.pages.push({
                pageNumber: i,
                currentClass: i === currentPage ? "active" : ""
            });
        }

        return options.fn(context);
    });

    this.template = this.dependencies.Handlebars.compile($template.html())
    this.$root = this.$self.children(this.selectors.ROOT)

    this.setState({
        page: 1,
        sort: "text",
        ascendingOrder: true
    });

    this.$self.on("afterrender", function (componentEvent) {
        var component = componentEvent.instance;

        component.$root.find(".page-button").click(function (event) {
            var pageButton = event.target
            var page = pageButton.hash.substring(1);
            component.setPageState({
                page: page,
            })
        });

        component.$root.find(("thead th")).click(function (event) {
            var $pageButton = $(event.target)
            var sort = $pageButton.text().toLowerCase();
            component.setSortState({
                sort: sort,
                ascendingOrder: !component.state.ascendingOrder
            })
        });
    })
};

com.github.component.Component.prototype.setState = function (state) {
    if (JSON.stringify(this.state) !== JSON.stringify(state)) {
        this.state = state
        this.render()
    }
};

com.github.component.Component.prototype.setPageState = function (state) {
    this.setState({
        page: state.page,
        sort: this.state.sort,
        ascendingOrder: this.state.ascendingOrder
    })
};

com.github.component.Component.prototype.setSortState = function (state) {
    this.setState({
        page: this.state.page,
        sort: state.sort,
        ascendingOrder: state.ascendingOrder
    })
};

com.github.component.Component.prototype.render = function () {
    var self = this
    this.state.size = $.urlParam('size');

    this.dependencies.api
        .fetchData(this.state)
        .then(this.template)
        .then(function (html) {
            self.$root.html(html)
            self.$self.trigger({
                type: "afterrender",
                instance: self
            })
        })
};

$.urlParam = function(name){
	var results = new RegExp('[?&]' + name + '=([^&#]*)').exec(window.location.href);
    return results !== null ? results[1] : 0;
};
