import numpy as np
import matplotlib.pyplot as plt

from scipy.io import netcdf

f = netcdf.netcdf_file('../densities.nc', 'r')

#vTime = f.variables['time']
vFeed = f.variables['feed']
print vFeed.shape

for i in range(0,4):
    plt.subplot(2,2,1+i)
    toPlot = vFeed[0,:,:,1+i*3]
    im = plt.imshow(toPlot)

plt.show()
