(ns cljs-ktn-trumpet.api
  (:require [ajax.core :refer [POST]]
            [goog.object :as o]))

;; User
(defn get-login-user []
  ((o/get js/kintone "getLoginUser")))

;; Records
(defn get-record-id []
  ((-> (.-app js/kintone)
       (o/get "record")
       (o/get "getId"))))

(defn get-record []
  ((-> (.-app js/kintone)
       (o/get "record")
       (o/get "get"))))

;; App
(defn get-app-id []
  ((-> (.-app js/kintone)
       (o/get "getId"))))

(defn get-query []
  ((-> (.-app js/kintone)
       (o/get "getQuery"))))

(defn get-query-condition []
  ((-> (.-app js/kintone)
       (o/get "getQueryCondition"))))

;; Events
(defn delete-all-event-handlers []
  ((-> js/kintone
       (o/get "events")
       (o/get "off"))))

(defn delete-record-detail-show-event-handlers []
  ((-> js/kintone
       (o/get "events")
       (o/get "off"))
   "app.record.detail.show"))

(defn delete-record-index-show-event-handlers []
  ((-> js/kintone
       (o/get "events")
       (o/get "off"))
   "app.record.index.show"))

(defn register-record-detail-show-event [handler]
  ((-> js/kintone
       (o/get "events")
       (o/get "on"))
   "app.record.detail.show"
   handler))

(defn register-record-index-show-event [handler]
  ((-> js/kintone
       (o/get "events")
       (o/get "on"))
   "app.record.index.show"
   handler))

;; DOMs
(defn get-header-menu-space-element-detail []
  ((-> js/kintone
       (o/get "app")
       (o/get "record")
       (o/get "getHeaderMenuSpaceElement"))))

(defn get-header-menu-space-element-index []
  ((-> js/kintone
       (o/get "app")
       (o/get "getHeaderMenuSpaceElement"))))

(defn get-header-space-element-index []
  ((-> js/kintone
       (o/get "app")
       (o/get "getHeaderSpaceElement"))))

;; REST APIs
(defn get-request-token []
  ((o/get js/kintone "getRequestToken")))

(defn request [url method params success-handler error-handler]
  (let [ktn-api (-> js/kintone (o/get "api"))
        ktn-api-url (o/get ktn-api "url")]
    (ktn-api (ktn-api-url url true)
             method
             (clj->js params)
             success-handler
             error-handler)))

(defn get-records-request [app query fields total-count? success-handler error-handler]
  (request "/k/v1/records" "GET"
           {:app app :query query :fields fields :totalCount total-count?}
           success-handler error-handler))

(defn get-record-request [app id success-handler error-handler]
  (request "/k/v1/record" "GET"
           {:app app :id id}
           success-handler error-handler))

(defn make-url [route-str]
  ((-> js/kintone
       (o/get "api")
       (o/get "url"))
   route-str))

(defn file-upload-request [blob-obj file-name handler]
  (let [url (make-url "/k/v1/file")
        form-data (doto (js/FormData.)
                    (.append "__REQUEST_TOKEN__" (get-request-token))
                    (.append "file" blob-obj file-name))]
    (POST url
          {:body form-data
           :headers {"X-Requested-With" "XMLHttpRequest"}
           :handler handler
           :error-handler #(js/alert "file upload failed") })))

(defn get-app-fields [app-id success-handler error-hanlder]
  (request "/k/v1/app/form/fields"
           "GET"
           {:app app-id}
           success-handler
           error-hanlder))

(defn edit-record-request
  [app-id record-id record-updated success-handler error-handler]
  (request "/k/v1/record"
           "PUT"
           {:app app-id
            :id record-id
            :record record-updated}
           success-handler
           error-handler))

(defn append-file-to-record-field [app-id record-id record field-name file-key
                                   success-handler error-handler]
  (let [original-field-value (-> record (o/get "record") (o/get field-name) (o/get "value") js->clj)]
    (request "/k/v1/record"
             "PUT"
             {:app app-id
              :id record-id
              :record
              {field-name
               {:value (conj original-field-value
                             {:fileKey file-key})}}}
             success-handler
             error-handler)))

(defn get-space-members-request [space-id success-handler error-handler]
  (request "/k/v1/space/members"
           "GET"
           {:id space-id}
           success-handler
           error-handler))

(defn mount-css-link [url]
  (let [l (aget (.getElementsByTagName js/document "link") 0)]
    (.insertBefore (.-parentNode l)
                   (doto (.createElement js/document "link")
                     (o/set "type" "text/css")
                     (o/set "rel" "stylesheet")
                     (o/set "href" url))
                   l)))

(defn post-comment-request [space-id thread-id
                            text mentions
                            success-handler
                            error-handler]
  (request "/k/v1/space/thread/comment"
           "POST"
           {:space space-id
            :thread thread-id
            :comment {:text text
                      :mentions mentions}}
           success-handler
           error-handler))

(defn get-app-acl-request [app-id success-handler error-handler]
  (request "/k/v1/app/acl" "GET"
           {:app app-id}
           success-handler
           error-handler))

(defn get-all-records [app-id query success-handler error-handler]
  (let [step 500
        a (doto (atom {:records []})
            (add-watch ::watch (fn [_ _ _ {:keys [records total-count]}]
                                 (when (= total-count (str (count records)))
                                   (success-handler records)))))
        add-response-fn (fn [res]
                          (swap! a (fn [old-val]
                                     (->  old-val
                                          (update :records #(->  res
                                                                 (o/get "records")
                                                                 (js->clj :keywordize-keys true)
                                                                 (->> (into %))))
                                          (assoc :total-count (o/get res "totalCount"))))))
        request-fn (fn [q callback]
                     (get-records-request app-id q nil true callback error-handler))]
    (request-fn (str query " limit " step)
                (fn [res]
                  (add-response-fn res)
                  (let [remaining-count (- (js/parseInt (o/get res "totalCount")) step)
                        remaining-reqs (js/Math.ceil (/ remaining-count step))]
                    (doseq [i (range 1 (inc remaining-reqs))]
                      (request-fn (str query " limit " step " offset " (* step i))
                                  add-response-fn)))))))
