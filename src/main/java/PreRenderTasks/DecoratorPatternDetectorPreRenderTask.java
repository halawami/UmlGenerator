package PreRenderTasks;

import java.util.List;
import java.util.Optional;

import Containers.ArrowContainer;
import Containers.AssociationArrowContainer;
import Containers.ClassContainer;
import Containers.DependencyArrowContainer;
import Containers.FieldContainer;
import Containers.ImplementationArrowContainer;
import Containers.InheritanceArrowContainer;
import Containers.MethodContainer;
import Containers.ProgramContainer;
import Containers.StereotypeContainer;
import Enums.Modifier;
import Wrappers.MethodNodeWrapper;

public class DecoratorPatternDetectorPreRenderTask extends PreRenderTaskDecorator {

	public DecoratorPatternDetectorPreRenderTask(PreRenderTask preRenderTask) {
		super(preRenderTask);
	}

	@Override
	public ProgramContainer getProgramContainer() {
		ProgramContainer toReturn = super.getProgramContainer();
		
		for (ArrowContainer inheritanceOrImplementation : toReturn.arrows){
			if (inheritanceOrImplementation instanceof InheritanceArrowContainer
					|| inheritanceOrImplementation instanceof ImplementationArrowContainer) {
				for(ArrowContainer associationOrDependency : toReturn.arrows){
					if((associationOrDependency instanceof AssociationArrowContainer || associationOrDependency instanceof DependencyArrowContainer) && 
							associationOrDependency.from.equals(inheritanceOrImplementation.from) && associationOrDependency.to.equals(inheritanceOrImplementation.to)){
						ClassContainer decoratedClass = inheritanceOrImplementation.to;
						ClassContainer decorator = inheritanceOrImplementation.from;
						Boolean isDecorator = false;
						for(ArrowContainer inheritanceToDecorator : toReturn.arrows){
							if (inheritanceToDecorator instanceof InheritanceArrowContainer &&
									inheritanceToDecorator.to.equals(decorator) && 
//										overridesAllMethod(inheritanceToDecorator.from, decorator)
									overridesAllConcreteMethods(inheritanceToDecorator)
									) {
								ClassContainer concreteDecorator = inheritanceToDecorator.from;
								concreteDecorator.displayContainer.color = Optional.of("lightgreen");
								concreteDecorator.stereotypeContainer.add(new StereotypeContainer("decorator"));
								isDecorator = true;
							}
						}
						if(isDecorator){
							inheritanceOrImplementation.stereotypeContainer.add(new StereotypeContainer("decorates"));
							decoratedClass.displayContainer.color = Optional.of("lightgreen");
							decorator.displayContainer.color = Optional.of("lightgreen");
							decorator.stereotypeContainer.add(new StereotypeContainer("decorator"));
						}
					}
				}
			}
		}
		return toReturn;
	}
	
	private boolean overridesAllConcreteMethods(ArrowContainer arrowContainer) {
		List<MethodNodeWrapper> toMethodNodeWrappers = arrowContainer.to.classNodeWrapper.methodNodeWrappers;
		List<MethodNodeWrapper> fromMethodNodeWrappers = arrowContainer.from.classNodeWrapper.methodNodeWrappers;
		for (MethodNodeWrapper toMethodNodeWrapper : toMethodNodeWrappers) {
			if (!toMethodNodeWrapper.modifiers.contains(Modifier.ABSTRACT)) {
				for (MethodNodeWrapper fromMethodNodeWrapper : fromMethodNodeWrappers) {
					if (!fromMethodNodeWrapper.modifiers.contains(Modifier.ABSTRACT)) {
						if (fromMethodNodeWrapper.name.equals(toMethodNodeWrapper.name)
								&& !fromMethodNodeWrapper.name.equals("<init>")
								&& fromMethodNodeWrapper.parameterNodeWrappers
										.size() == toMethodNodeWrapper.parameterNodeWrappers.size()) {
							for (int i = 0; i < fromMethodNodeWrapper.parameterNodeWrappers.size(); i++) {
								if (!fromMethodNodeWrapper.parameterNodeWrappers.get(i).type
										.equals(toMethodNodeWrapper.parameterNodeWrappers.get(i).type)) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean overridesAllMethod(ClassContainer decorator, ClassContainer abstractDecorator){
		for(MethodContainer method : abstractDecorator.methods){
			if(!decorator.methods.contains(method)){
				return false;
			}
		}
		return true;
	}

}
