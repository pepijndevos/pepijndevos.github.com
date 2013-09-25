def php_flatten(data):
    res = {}

    def inner_flatten(path, value):
        if isinstance(value, dict):
            for k, v in value.items():
                newpath = "%s[%s]" % (path, k)
                inner_flatten(newpath, v)
        elif isinstance(value, list):
            for k, v in enumerate(value):
                newpath = "%s[%s]" % (path, k)
                inner_flatten(newpath, v)
        else:
            res[path] = value

    for k, v in data.items():
        inner_flatten(k, v)

    return res
