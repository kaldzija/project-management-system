/**
 * Created by jkaldzij on 02.09.2016..
 */

angular
    .module('inspinia')
    .filter('myStrictFilter', function ($filter) {
        return function (input, predicate) {
            var strict = false;
            if (predicate.status && !predicate.name)
                strict = true;
            return $filter('filter')(input, predicate, strict);
        }
    })
    .filter('unique', function () {
        return function (arr, field) {
            if (arr == null)
                return;
            var o = {}, i, l = arr.length, r = [];
            for (i = 0; i < l; i += 1) {
                o[arr[i][field]] = arr[i];
            }
            for (i in o) {
                r.push(o[i]);
            }
            return r;
        };
    });