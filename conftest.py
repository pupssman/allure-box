"""
Tests for allurebox

pointing to where box is deployed
"""

def pytest_addoption(parser):
    parser.addoption("--boxurl", action="store", default=None)
