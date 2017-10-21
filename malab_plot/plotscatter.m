

%draw scatterplot of moea/d

%variable=FUN_uniform;
variable=FUN_MOEAD_d;


a=variable(:,1);
b=variable(:,2);
c=variable(:,3);
d=variable(:,4);


scatter3(log(a),log(b),log(d),5,log(c),'filled');%color+3d


xlabel(gca,'logFNR');
ylabel(gca,'logFPR');
zlabel(gca,'logUR');
set(gca,'ZDir','reverse')


colorbar;
