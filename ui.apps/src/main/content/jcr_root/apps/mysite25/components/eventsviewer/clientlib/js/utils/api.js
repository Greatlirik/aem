(function ($) {
    var module = com.github.component
    var constants = module.constants.API

    module.api = {
                fetchData: function (params) {
                    return $.getJSON(constants.DATA, params)
                }
    }
})(jQuery);
