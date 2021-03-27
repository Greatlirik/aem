(function ($, Handlebars) {
    var module = com.github.component
    var constants = module.constants
    var SELECTOR = constants.SELECTORS.SELF

    $(function () {
        module.InstancePool = $(SELECTOR)
            .toArray()
            .map(function (el) {
                return new module.Component({
                    $self: $(el),
                    dependencies: {
                        api: module.api,
                        Handlebars: Handlebars
                    }
                })
            })

    })
})(jQuery, Handlebars);
