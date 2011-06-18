(ns nio
  (:import
     [java.net
      InetSocketAddress]
     [java.nio
      ByteBuffer
      CharBuffer]
     [java.nio.channels
      ServerSocketChannel
      Selector
      SelectionKey]
     [java.nio.charset
      Charset]))

(def *buffer* (ByteBuffer/allocate 16384))

(defn selector [server-socket-channel]
  (let [selector (Selector/open)]
    (.register server-socket-channel selector SelectionKey/OP_ACCEPT)
    selector))

(defn setup [port]
  (let [server-socket-channel (ServerSocketChannel/open)
        _ (.configureBlocking server-socket-channel false)
        server-socket (.socket server-socket-channel)
        inet-socket-address (InetSocketAddress. port)]
    (.bind server-socket inet-socket-address)
    [(selector server-socket-channel)
     server-socket]))

(defn state= [state channel]
  (= (bit-and (.readyOps channel) state) state))

(defn buffer->string
  ([byte-buffer]
   (buffer->string byte-buffer (Charset/defaultCharset)))
  ([byte-buffer charset]
   (.toString (.decode charset byte-buffer))))

(defn string->buffer
  ([string]
   (string->buffer string (Charset/defaultCharset)))
  ([string charset]
   (.encode charset string)))

(defn accept-connection [server-socket selector]
  (let [channel (-> server-socket (.accept) (.getChannel))]
    (println "Connection from" channel)
    (doto channel
      (.configureBlocking false)
      (.register selector SelectionKey/OP_READ))))

(defn read-socket [selected-key]
  (let [socket-channel (.channel selected-key)]
    (.clear *buffer*)
    (.read socket-channel *buffer*)
    (.flip *buffer*)
    (if (= (.limit *buffer*) 0)
      (do
        (println "Lost connection from" socket-channel)
        (.cancel selected-key)
        (.close (.socket socket-channel)))
      (.write socket-channel *buffer*))))

(defn react [selector server-socket]
  (while true
    (when (> (.select selector) 0)
      (let [selected-keys (.selectedKeys selector)]
        (doseq [k selected-keys]
          (condp state= k
            SelectionKey/OP_ACCEPT
            (accept-connection server-socket selector)
            SelectionKey/OP_READ
            (read-socket k)))
        (.clear selected-keys)))))

(defn run []
  (apply react (setup 2323)))
