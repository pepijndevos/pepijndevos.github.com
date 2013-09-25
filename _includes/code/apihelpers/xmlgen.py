import lxml.etree as ET

def maybesub(el, tag):
    """
    Create a subelement if a tag was given.
    Otherwise return the current node.
    Used for the root node.
    """
    if tag:
        return ET.SubElement(el, tag)
    else:
        return el

def walk(el, tag, data):
    """
    Recursively add child nodes named tag to el based on data.
    """
    if isinstance(data, dict):
        sub = maybesub(el, tag)
        for key, value in sorted(data.items()):
            walk(sub, key, value)
    elif isinstance(data, list):
        for value in data:
            walk(el, tag, value)
    else:
        sub = maybesub(el, tag)
        sub.text = str(data)

def dumps(data, root="response"):
    root = ET.Element(root)
    walk(root, None, data)
    return ET.tostring(root, pretty_print=True)
