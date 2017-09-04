# cljs-ktn-trumpet

## What?
- A ClojureScript wrapper for a subset of [Kintone JavaScript API](https://developer.cybozu.io/hc/ja/articles/201941754)
- Some convenience utilities built on top of the wrapper
- Survives compilation with `:advanced` mode `:optimizations`

## Why?

- Invoking the JavaScript API naively like `(.getId js/kintone.app.record)` is bound to fail when you compile your code with `:advanced` mode.
- Did not want to write externs :P
- Would like to use ClojureScript to customize kintone

## Install

[![Clojars Project](https://img.shields.io/clojars/v/iku000888/cljs-ktn-trumpet.svg)](https://clojars.org/iku000888/cljs-ktn-trumpet)

## Which functions are wrapped?

Everything is included in the `cljs-ktn-trumpet.api` namespace.
Included api's (and mapping to authentic api when applicable)

| fn        | maps to          | Remarks                 |
| --------------- |:---------------:| -------------------- |
| get-login-user | kintone.getLoginUser() |         |
| get-record-id | kintone.app.record.getId() |  |
| get-record | kintone.app.record.get() ||
| get-app-id | kintone.app.getId() ||
| get-query | kintone.app.getQuery() ||
| get-query-condition | kintone.app.getQueryCondition() ||
| delete-all-event-handlers | kintone.events.off() ||
| delete-record-detail-show-event-handlers | kintone.events.off("app.record.detail.show") ||
| delete-record-index-show-event-handlers | kintone.events.off("app.record.index.show") ||
| register-record-detail-show-event | kintone.events.on("app.record.detail.show",handler)||
| get-header-menu-space-element-detail | kintone.app.record.getHeaderMenuSpaceElement()||
| get-header-menu-space-element-index | kintone.app.getHeaderMenuSpaceElement()||
| get-header-space-element-detail | kintone.app.getHeaderSpaceElement()||
| get-request-token | kintone.getRequestToken()||
| request | kintone.api(...)||
| get-record-request |[documentation](https://developer.cybozu.io/hc/ja/articles/202331474#step1)| wraps the request fn for convenience|
| get-records-request |[documentation](https://developer.cybozu.io/hc/ja/articles/202331474#step2)| wraps the request fn for convenience|
| edit-records-request |[documentation](https://developer.cybozu.io/hc/ja/articles/201941784)||
| file-upload-request |[documentation](https://developer.cybozu.io/hc/ja/articles/201941824)||
| get-app-fields|[documentation](https://developer.cybozu.io/hc/ja/articles/204783170)||
| append-file-to-record-field || Attach a file to a field without removing any previous files|
| get-space-members-request |[documentation](https://developer.cybozu.io/hc/ja/articles/202166220)||
| mount-css-link || Adds a link dom node for the external css url. Added for convenience|
| post-commnet-request |[documentation](https://developer.cybozu.io/hc/ja/articles/209732306)||

## I need api x wrapped for cljs, but do not see it in here!
- Issues and pull requests welcome :)

## License

Copyright © 2017 Ikuru K

MIT Lliscence
