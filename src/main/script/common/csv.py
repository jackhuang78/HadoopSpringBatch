#!/usr/bin/python
import sys, os
if 'pylib' in os.environ:
	sys.path.append('%s' % os.environ['pylib'])
#print 'Python lib: ', os.environ['pylib']

import argparse

parser = argparse.ArgumentParser(description='Change a formatted file\'s delimiter.')
parser.add_argument('from', metavar='FROM')
parser.add_argument('to', metavar='TO')
parser.add_argument('-q', action='store_true', help='Use quote to escape delimiters.')
parser.add_argument('-n', action='store', type=int, help='Number of lines to skip.')

print sys.argv
print parser.parse_args()