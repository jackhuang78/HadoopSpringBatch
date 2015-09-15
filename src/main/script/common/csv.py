#!/usr/bin/python

# add the user python library path, if given
import sys, os
if 'pylib' in os.environ:
	sys.path.append('%s' % os.environ['pylib'])

# import libraries
import argparse, errno

# parse arguments
parser = argparse.ArgumentParser(description='Change a formatted file\'s delimiter.')
parser.add_argument('fromDelim', metavar='FROM', help="Delimiter to convert from. Can also be \'tab\' or \'ctrlA\'.")
parser.add_argument('toDelim', metavar='TO', help="Delimiter to convert to. Can also be \'tab\' or \'ctrlA\'.")
parser.add_argument('files', metavar='FILE', nargs='+', help='Files/directories to convert.')
parser.add_argument('-d', action='store', dest="outDir", default='.', help='Output directory.')
parser.add_argument('-s', action='store', dest='outSuf', default='.converted', help='Suffix appended to the output filename.')
parser.add_argument('-b', action='store_true', dest='bundle', help='If given a directory, concat all its files into one output.')
parser.add_argument('-n', dest='linesToSkip', action='store', type=int, default=0, help='Number of lines to skip.')
parser.add_argument('-q', action='store_true', dest='useQuote', help='If input file uses quote to escape delimiters as acutal values.')
args = parser.parse_args()
#print 'args: ', args

# convert special delimiters to their actual values
delimMap = {
	'tab': '\t',
	'ctrlA': '\x01'
}
if args.fromDelim in delimMap:
	args.fromDelim = delimMap[args.fromDelim]
if args.toDelim in delimMap:
	args.toDelim = delimMap[args.toDelim]

# pair up input and output files
files = []
for f in args.files:
	if not os.path.isdir(f):
		# input is a file, it is to be converted to a output ifle
		files.append(([f], f + args.outSuf))
	else:
		# if input is a directory, convert all files under it
		parts = map(lambda ff: os.path.join(f,ff), filter(lambda f: f[0] != '.', os.listdir(f)))
		if args.bundle:
			# if bundle is enabled, all child files go to the same output
			files.append((parts, f + args.outSuf))
		else:
			# each child files goes to its own output
			for ff in parts:
				files.append(([ff], ff + args.outSuf))
files = map(lambda (fromFiles, toFile): (fromFiles, os.path.join(args.outDir, os.path.basename(toFile))), files)

try:
	os.mkdir(args.outDir)
except OSError as e:
	if e.errno != errno.EEXIST:
		raise e
	pass

# convert input file to output file
for fromFiles, toFile in files:
	print 'converting: ', fromFiles, '->', toFile
	with open(toFile, 'w') as outfile:
		for fromFile in fromFiles:
			i = 0
			with open(fromFile) as infile:
				for line in infile:
					i += 1
					if i <= args.linesToSkip: 
						continue

					line = line.strip()
					inQuote = False;
					newLine = '';
					for c in list(line):
						if args.useQuote and inQuote:
							if c == '"':
								inQuote = False;
							elif c == opts.toDelim:
								print 'Detect TODELIMITER(%s) in %s line %d. Ignoring it the output file...' % (args.toDelim, fromFile, i)  
							else:
								newLine += c;

						else:
							if args.useQuote and c == '"':
								inQuote = True;
							elif c == args.fromDelim:
								#print 'to delimi(%s)' % opts.toDelim
								newLine += args.toDelim
							elif c == args.toDelim:
								print 'Detect TODELIMITER(%s) in %s line %d. Ignoring it the output file...' % (args.toDelim, fromFile, i)  
							else:
								newLine += c;
					outfile.write(newLine + '\n');


