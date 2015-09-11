import uuid

@outputSchema('uuid:chararray')
def uid():
	return uuid.uuid1().hex.upper()