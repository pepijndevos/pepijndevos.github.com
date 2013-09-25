import redis
from functools import wraps
import pickle
import logging

pool = redis.ConnectionPool()

def memoize(ttl=3600):
    def decorator(f):
        @wraps(f)
        def wrapper(*args, **kwargs):
            try:
                r = redis.StrictRedis(connection_pool=pool)
                # Compute the function signature
                sig = (f.__module__, f.__name__, args, kwargs)
                pargs = pickle.dumps(sig) 
                # Try to get the result
                pres = r.get(pargs)
                if pres:
                    return pickle.loads(pres)
                else:
                    # Or compute and store
                    res = f(*args, **kwargs)
                    r.setex(pargs, ttl, pickle.dumps(res))
                    return res
            except redis.RedisError:
                # Show must go on!
                logging.exception("redis oopsed")
                return f(*args, **kwargs)

        return wrapper
    return decorator
