import numpy as np
import matplotlib.pyplot as plt

subsample = 10

data = np.genfromtxt("../pellets.csv", dtype=None, delimiter='\t')
data = data[:,:-1]
xpos = data[:,0::3*subsample]
ypos = data[:,1::3*subsample]
zpos = data[:,2::3*subsample]

#plt.plot(xpos,ypos)
#plt.plot(xpos,zpos)
plt.plot(zpos)

plt.show()
