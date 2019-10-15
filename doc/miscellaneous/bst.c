#include <stdlib.h>
#include <string.h>
#include <unistd.h>
typedef int _int;
struct __string {
	int length;
	char* value;
};
typedef struct __string* _string;
_string newstring (int length, char* value) {
	_string str = malloc (sizeof (struct __string));
	*str = (struct __string) {length, value};
	return str;
}
_string _chr (_int integer) {
	int length = 1;
	char* value = malloc (sizeof (char));
	*value = integer;
	return newstring (length, value);
}
_string _concat (_string str1, _string str2) {
	int length = str1->length + str2->length;
	char* value = malloc (length * sizeof (char));
	memcpy (value, str1->value, str1->length * sizeof (char));
	memcpy (&value [str1->length], str2->value, str2->length * sizeof (char));
	return newstring (length, value);
}
_int _not (_int integer) {
	return !integer;
}
void _print (_string str) {
	write (1, str->value, str->length);
}
void main () {
	_string __0 = newstring (1, "\t");
	_string __1 = newstring (1, "\n");
	_string __2 = newstring (0, "");
	_string __3 = newstring (1, "0");
	_string __4 = newstring (0, "");
	_string __5 = newstring (1, "-");
	void _0 () {
		struct __BST {
			_int value;
			struct __BST* left;
			struct __BST* right;
		};
		typedef struct __BST* _BST;
		_string _itoa (_int _integer) {
			_string _0 (_int* _integer) {
				_string _0 (_int* _integer) {
					void _0 (_int* _integer, _string* _str, _int* _neg) {
						if (*_integer < 0) (
							*_integer = -*_integer,
							*_neg = 1
						);
					}
					void _1 (_int* _integer, _string* _str, _int* _neg) {
						void _0 (_int* _integer, _string* _str, _int* _neg) {
							_int _quot = 0;
							_int _rem = 0;
							(
								_quot = *_integer / 10,
								_rem = *_integer - _quot * 10,
								*_integer = _quot,
								*_str = _concat (_chr (_rem + 48), *_str)
							);
						}
						while (*_integer) _0 (_integer, _str, _neg);
					}
					void _2 (_int* _integer, _string* _str, _int* _neg) {
						if (*_neg) (
							*_str = _concat (__5, *_str)
						);
					}
					_string _str = __4;
					_int _neg = 0;
					return (
						_0 (_integer, &_str, &_neg),
						_1 (_integer, &_str, &_neg),
						_2 (_integer, &_str, &_neg),
						_str
					);
				}
				if (_not (*_integer)) return __3;
				else return _0 (_integer);
			}
			return _0 (&_integer);
		}
		_BST _addToBST (_BST _tree, _int _value) {
			_BST _0 (_BST* _tree, _int* _value) {
				_BST _0 (_BST* _tree, _int* _value, _BST* _leaf) {
					_BST _0 (_BST* _tree, _int* _value, _BST* _leaf) {
						void _0 (_BST* _tree, _int* _value, _BST* _leaf, _int* _side, _BST* _current, _BST* _next) {
							_BST _0 (_BST* _tree, _int* _value, _BST* _leaf, _int* _side, _BST* _current, _BST* _next) {
								if (*_side) return (*_current)->left;
								else return (*_current)->right;
							}
							while (*_next != 0) (
								*_current = *_next,
								*_side = *_value < (*_current)->value,
								*_next = _0 (_tree, _value, _leaf, _side, _current, _next)
							);
						}
						void _1 (_BST* _tree, _int* _value, _BST* _leaf, _int* _side, _BST* _current, _BST* _next) {
							if (*_side) (
								(*_current)->left = *_leaf
							);
							else (
								(*_current)->right = *_leaf
							);
						}
						_int _side = 0;
						_BST _current = 0;
						_BST _next = *_tree;
						return (
							_0 (_tree, _value, _leaf, &_side, &_current, &_next),
							_1 (_tree, _value, _leaf, &_side, &_current, &_next),
							*_tree
						);
					}
					if (*_tree == 0) return *_leaf;
					else return _0 (_tree, _value, _leaf);
				}
				_BST _leaf = malloc (sizeof (struct __BST));
				*_leaf = (struct __BST) {*_value, 0, 0};
				return (
					_0 (_tree, _value, &_leaf)
				);
			}
			return _0 (&_tree, &_value);
		}
		void _printBST (_BST _tree) {
			void _0 (_BST* _tree) {
				void _printBST (_BST _tree, _string _currentPadding) {
					void _0 (_BST* _tree, _string* _currentPadding) {
						void _0 (_BST* _tree, _string* _currentPadding) {
							void _0 (_BST* _tree, _string* _currentPadding) {
								_string _nextPadding = _concat (*_currentPadding, __0);
								(
									_printBST ((*_tree)->left, _nextPadding),
									_print (*_currentPadding),
									_print (_itoa ((*_tree)->value)),
									_print (__1),
									_printBST ((*_tree)->right, _nextPadding)
								);
							}
							if (*_tree != 0) _0 (_tree, _currentPadding);
						}
						_0 (_tree, _currentPadding);
					}
					_0 (&_tree, &_currentPadding);
				}
				_printBST (*_tree, __2);
			}
			_0 (&_tree);
		}
		_BST _tree = 0;
		(
			_tree = _addToBST (_tree, 16),
			_tree = _addToBST (_tree, 1),
			_tree = _addToBST (_tree, 5),
			_tree = _addToBST (_tree, 21),
			_tree = _addToBST (_tree, 4),
			_tree = _addToBST (_tree, 10),
			_tree = _addToBST (_tree, -49),
			_tree = _addToBST (_tree, 32),
			_tree = _addToBST (_tree, 65),
			_printBST (_tree)
		);
	}
	_0 ();
}
