>>> val1 = []
>>> val2 = []
>>> index1 = {1:val1, 5:val2}
>>> index2 = {"pepijn":val1, "ben":val2}
>>> index2["pepijn"].append("foo")
>>> index1[1]
['foo']
