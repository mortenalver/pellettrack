import numpy as np
import matplotlib.pyplot as plt

## 
#def filterData(data, dim, interval):
    

subsample = 10

data = np.genfromtxt("../pellets.csv", dtype=None, delimiter='\t')
data = data[:,:-1]
xpos = data[:,0::3*subsample]
ypos = data[:,1::3*subsample]
zpos = data[:,2::3*subsample]

#plt.plot(xpos,ypos)
#plt.plot(xpos,zpos)
#plt.plot(zpos)

plottime = 150

t_xpos = data[plottime,0::3]
t_ypos = data[plottime,1::3]
t_zpos = data[plottime,2::3]

plt.subplot(1,2,1)
plt.plot(t_xpos, t_zpos, 'x')
plt.subplot(1,2,2)
plt.plot(t_xpos, t_ypos, 'x')

plt.show()
